package ecommerce.service

import ecommerce.dto.ProductDTO
import ecommerce.dto.ProductPatchDTO
import ecommerce.exception.DuplicateProductNameException
import ecommerce.exception.EntityNotFoundException
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service
import java.net.URI

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getAllProducts(): List<ProductDTO> {
        return productRepository.findAll()
    }

    fun getProductById(id: Long): ProductDTO {
        return productRepository.findById(id)
            ?: throw EntityNotFoundException("Product with id: $id not found")
    }

    fun createProduct(product: ProductDTO): URI {
        if (productRepository.existsByName(product.name)) {
            throw DuplicateProductNameException(product.name)
        }
        val id = productRepository.create(product)
        return URI.create("/products/$id")
    }

    fun updateProduct(
        id: Long,
        product: ProductDTO,
    ) {
        if (productRepository.update(id, product) == 0) {
            throw EntityNotFoundException("Product with id: $id not found")
        }
    }

    fun patchProduct(
        id: Long,
        patch: ProductPatchDTO,
    ) {
        val existing =
            productRepository.findById(id)
                ?: throw EntityNotFoundException("Product with id: $id not found")

        val updatedProduct =
            existing.copy(
                name = patch.name ?: existing.name,
                description = patch.description ?: existing.description,
                price = patch.price ?: existing.price,
                imageUrl = patch.imageUrl ?: existing.imageUrl,
                quantity = patch.quantity ?: existing.quantity,
            )

        productRepository.update(id, updatedProduct)
    }

    fun deleteProduct(id: Long) {
        if (productRepository.deleteById(id) == 0) {
            throw EntityNotFoundException("Product with id: $id not found")
        }
    }
}
