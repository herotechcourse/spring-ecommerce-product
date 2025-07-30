package ecommerce.dto

import ecommerce.sql.SortOption

class StatsQueryParams(
    val days: Int,
    val limit: Int,
    val sort: SortOption,
)
