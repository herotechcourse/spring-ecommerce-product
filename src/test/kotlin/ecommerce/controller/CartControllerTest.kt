package ecommerce.controller

import ecommerce.entity.CartItem
import ecommerce.entity.Product
import ecommerce.entity.Role
import ecommerce.entity.User
import ecommerce.repository.CartRepository
import ecommerce.repository.ProductRepository
import ecommerce.repository.UserRepository
import ecommerce.service.JwtService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // rolls back DB changes after each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CartControllerTest
    @Autowired
    constructor(
        val mockMvc: MockMvc,
        val userRepository: UserRepository,
        val productRepository: ProductRepository,
        val cartRepository: CartRepository,
        val jwtService: JwtService,
    ) {
        lateinit var user: User
        lateinit var token: String
        lateinit var product1: Product
        lateinit var product2: Product

        @BeforeEach
        fun setup() {
            user = User(email = "user@mail.com", password = "p123456", role = Role.USER)
            user.id = userRepository.create(user)

            token = jwtService.generateToken(user.email)

            product1 =
                Product(
                    name = "walnut ice", price = 1.25,
                    imageUrl =
                        "https://m1.quebecormedia.com/emp/cl_prod/" +
                            "0c812707039196cfd54e13449059d16b5754cfc6/" +
                            "Screenshot-2023-06-02-at-2-31-16-PM.jpg?impolicy=resize&width=1500&height=1500",
                )
            product1.id = productRepository.create(product1)

            product2 =
                Product(
                    name = "almond ice", price = 1.50,
                    imageUrl =
                        "https://encrypted-tbn0.gstatic.com/" +
                            "images?q=tbn:ANd9GcRuUj5GRmKPJKaPk4PG_Ref3J0c-HnONIh29A&s",
                )
            product2.id = productRepository.create(product2)
        }

        @Test
        fun `add new item to cart`() {
            val json = """{"productId":${product1.id}, "quantity":1}"""

            mockMvc.perform(
                post("/api/cart")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json),
            )
                .andExpect(status().isCreated)

            val items = cartRepository.findByUserId(user.id!!)
            val item = items.firstOrNull { it.productId == product1.id }
            assert(item != null && item.quantity == 1)
        }

        @Test
        fun `add cart item without authorization returns 401`() {
            val json = """{"productId":${product1.id}, "quantity":1}"""

            mockMvc.perform(
                post("/api/cart")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json),
            )
                .andExpect(status().isUnauthorized)
        }

        @Test
        fun `add existing item increases quantity`() {
            // add first cart item
            cartRepository.create(CartItem(id = 0L, userId = user.id!!, productId = product1.id!!, quantity = 1))

            val json = """{"productId":${product1.id}, "quantity":1}"""

            // add another cart item of same kind
            mockMvc.perform(
                post("/api/cart")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json),
            )
                .andExpect(status().isCreated)

            val item = cartRepository.findByUserId(user.id!!).first { it.productId == product1.id }
            assert(item.quantity == 2)
        }

        @Test
        fun `get all cart items`() {
            cartRepository.create(CartItem(id = 0L, userId = user.id!!, productId = product1.id!!, quantity = 3))
            cartRepository.create(CartItem(id = 0L, userId = user.id!!, productId = product2.id!!, quantity = 2))

            mockMvc.perform(
                get("/api/cart")
                    .header("Authorization", "Bearer $token"),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].productId").value(product1.id))
                .andExpect(jsonPath("$[0].quantity").value(3))
                .andExpect(jsonPath("$[1].productId").value(product2.id))
                .andExpect(jsonPath("$[1].quantity").value(2))
        }

        @Test
        fun `delete existing cart item`() {
            cartRepository.create(CartItem(id = 0L, userId = user.id!!, productId = product1.id!!, quantity = 1))

            mockMvc.perform(
                delete("/api/cart/${product1.id}")
                    .header("Authorization", "Bearer $token"),
            )
                .andExpect(status().isNoContent)

            val items = cartRepository.findByUserId(user.id!!)
            assert(items.none { it.productId == product1.id })
        }

        @Test
        fun `delete non-existent cart item returns 404`() {
            mockMvc.perform(
                delete("/api/cart/1000000")
                    .header("Authorization", "Bearer $token"),
            )
                .andExpect(status().isNotFound)
        }

        @Test
        fun `update quantity of existing cart item`() {
            cartRepository.create(CartItem(id = 0L, userId = user.id!!, productId = product1.id!!, quantity = 1))

            val json = """{"quantity":5}"""

            mockMvc.perform(
                patch("/api/cart/${product1.id}")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json),
            )
                .andExpect(status().isOk)

            val item = cartRepository.findByUserId(user.id!!).first { it.productId == product1.id }
            assert(item.quantity == 5)
        }

        @Test
        fun `update quantity to zero removes cart item`() {
            cartRepository.create(CartItem(id = 0L, userId = user.id!!, productId = product1.id!!, quantity = 3))

            val json = """{"quantity":0}"""

            mockMvc.perform(
                patch("/api/cart/${product1.id}")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json),
            )
                .andExpect(status().isNoContent)

            val items = cartRepository.findByUserId(user.id!!)
            assert(items.none { it.productId == product1.id })
        }

        @Test
        fun `update quantity of non-existent cart item returns 404`() {
            val json = """{"quantity":3}"""

            mockMvc.perform(
                patch("/api/cart/1000000")
                    .header("Authorization", "Bearer $token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json),
            )
                .andExpect(status().isNotFound)
        }
    }
