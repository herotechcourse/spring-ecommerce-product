package ecommerce.dto

class AllCartItemsDto(
    val cartId: Long,
    val items: List<AllCartItemsDto> = emptyList(),
)
