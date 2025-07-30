package ecommerce.exception

class ResourceNotFoundException(resourceName: String, fieldName: String, fieldValue: Any) :
    RuntimeException("$resourceName not found with $fieldName : '$fieldValue'")
