package ecommerce.service

import ecommerce.domain.NewProduct
import ecommerce.dto.ProductResponse
import ecommerce.dto.mapper.ProductMapper
import ecommerce.entity.Product
import ecommerce.exception.NotFoundException
import ecommerce.exception.ProductAlreadyExistsException
import ecommerce.exception.RetrievalFailedException
import ecommerce.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val repository: ProductRepository) {
    fun insertNewProduct(newProduct: NewProduct): Product {
        if (repository.existsByName(newProduct.name)) {
            throw ProductAlreadyExistsException("Product with name ${newProduct.name} already exists")
        }
        val id = repository.insertWithKeyholder(newProduct)
        return repository.findById(id)
            ?: throw RetrievalFailedException("Product with id $id not found")
    }

    fun getAllProducts(): List<ProductResponse> =
        repository
            .findAll()
            .map {
                ProductMapper.toResponse(it)
            }

    fun updateProduct(
        id: Long,
        newProduct: NewProduct,
    ): Product {
        if (repository.existsById(id).not()) {
            throw NotFoundException("Product with id $id not found")
        }
        return repository.putById(id, newProduct)
            ?: throw RetrievalFailedException("Could not update product with id $id")
    }

    fun deleteProductById(id: Long) {
        if (repository.existsById(id).not()) {
            throw NotFoundException("Product with id $id not found")
        }
        repository.deleteById(id)
    }
}
