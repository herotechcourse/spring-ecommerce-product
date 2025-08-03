package ecommerce.service

import ecommerce.dto.ProductRequest
import ecommerce.exception.DuplicateProductNameException
import ecommerce.exception.NotFoundException
import ecommerce.model.Product
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val jdbc: ProductRepository,
) {
    fun validateUniqueName(request: ProductRequest) {
        val product = Product.toEntity(request)
        when {
            jdbc.existsByName(product) -> throw DuplicateProductNameException(
                "Product name '${product.name}' already exists.",
            )
        }
    }

    fun create(request: ProductRequest): Long {
        return jdbc.create(Product.toEntity(request)) ?: throw RuntimeException()
    }

    fun getAll(): List<Product> {
        return jdbc.getAll()
    }

    fun update(
        id: Long,
        request: ProductRequest,
    ) {
        val product = Product.toEntity(request)
        if (!jdbc.update(id, product)) throw NotFoundException("Product with id $id not found")
    }

    fun delete(id: Long) {
        if (!jdbc.delete(id)) throw NotFoundException("Product with id $id not found")
    }
}
