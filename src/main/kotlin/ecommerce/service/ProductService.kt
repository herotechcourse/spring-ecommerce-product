package ecommerce.service

import ecommerce.model.Product
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class ProductService {
    private val products: MutableMap<Long, Product> = HashMap()
    private val index: AtomicLong = AtomicLong(1)

    fun getAllProducts(): List<Product> = products.values.toList()

    fun getProductById(id: Long): Product? = products.values.find { it.id == id }

    fun createProduct(newProduct: Product): Product {
        val product: Product = create(newProduct)
        products.put(index.andIncrement, product)
        println(products)
        return product
    }

    private fun create(product: Product): Product {
        return Product(product.id, product.name, product.price, product.img, product.quantity)
    }

    fun updateProduct(
        id: Long,
        updatedProduct: Product,
    ): Product? {
        val product = getProductById(id)
        product?.let {
            it.name = updatedProduct.name
            it.price = updatedProduct.price
            it.img = updatedProduct.img
            it.quantity = updatedProduct.quantity
        }
        return product
    }

    fun deleteProduct(id: Long): Boolean = products.values.removeIf { it.id == id }
}
