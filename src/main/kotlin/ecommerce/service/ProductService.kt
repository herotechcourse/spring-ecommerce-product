package ecommerce.service

import ecommerce.dto.ProductDTO
import ecommerce.service.interfaces.ProductServiceInterface
import ecommerce.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URI

@Service
class ProductService(private val productRepository: ProductRepository) : ProductServiceInterface {
    override fun getAllProducts(): ResponseEntity<List<ProductDTO>> {
        return ResponseEntity.ok().body(productRepository.findAll())
    }

    override fun getProductById(id: Long): ResponseEntity<ProductDTO> {
        return ResponseEntity.ok().body(productRepository.findById(id))
    }

    override fun createProduct(product: ProductDTO): ResponseEntity<Void> {
        val id = productRepository.create(product)
        return ResponseEntity.created(URI.create("/products/${id}")).build()
    }

    override fun updateProduct(
        id: Long,
        product: ProductDTO,
    ): ResponseEntity<Void> {
        productRepository.update(id, product)
        return ResponseEntity.ok().build()
    }

    override fun deleteProduct(id: Long): ResponseEntity<Void> {
        productRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
