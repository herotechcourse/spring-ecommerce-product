package ecommerce.controller.api

import ecommerce.application.annotation.LoggedMember
import ecommerce.dto.CartDTO
import ecommerce.dto.ProductDTO
import ecommerce.dto.ProductPatchDTO
import ecommerce.dto.ProductRequestDTO
import ecommerce.model.Cart
import ecommerce.model.Member
import ecommerce.service.CartService
import ecommerce.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService,
) {
    @GetMapping("/mine")
    fun getCartDto(
        @LoggedMember member: Member,
    ): ResponseEntity<CartDTO> {
        val cartDto = cartService.getCartDTO(member.id!!)
        return ResponseEntity.ok(cartDto)
    }

    @PostMapping("/items")
    fun addItem(
        @LoggedMember member: Member,
        @RequestParam productId: Long,
        @RequestParam quantity: Int,
    ): ResponseEntity<Cart> {
        val cart = cartService.addItem(member.id!!, productId, quantity)
        return ResponseEntity.ok(cart)
    }

    @DeleteMapping("/items/{productId}")
    fun removeItem(
        @LoggedMember member: Member,
        @PathVariable productId: Long,
    ): ResponseEntity<Void> {
        cartService.removeItem(member.id!!, productId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/clear")
    fun clearCart(
        @LoggedMember member: Member,
    ): ResponseEntity<Void> {
        cartService.clear(member.id!!)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/admin/api")
class ProductController(private val productService: ProductService) {
    @PostMapping("/products")
    fun createProduct(
        @Valid @RequestBody productRequest: ProductRequestDTO,
    ): ResponseEntity<ProductDTO> {
        val created = productService.insert(productRequest)
        val dto = ProductDTO.Companion.from(created)
        return ResponseEntity.created(URI("/products/${dto.id}")).body(dto)
    }

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<ProductDTO>> {
        val products = productService.findAll()
        val dtos = products.map { ProductDTO.Companion.from(it) }
        return ResponseEntity.ok(dtos)
    }

    @GetMapping("/products/{id}")
    fun getProduct(
        @PathVariable id: Long,
    ): ResponseEntity<ProductDTO> {
        val product = productService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ProductDTO.Companion.from(product))
    }

    @PatchMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody newProduct: ProductPatchDTO,
    ): ResponseEntity<Void> {
        val updated = productService.update(id, newProduct)
        if (updated) {
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        val deleted = productService.delete(id)
        if (deleted) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.notFound().build()
    }
}
