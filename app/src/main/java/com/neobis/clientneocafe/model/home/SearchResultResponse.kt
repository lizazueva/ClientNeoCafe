package com.neobis.clientneocafe.model.home

data class SearchResultResponse(
    val _highlightResult: HighlightResult,
    val branch_id: Int,
    val category_name: String,
    val description: String,
    val id: Int,
    val image: String?,
    val ingredients: List<Any>,
    val name: String,
    val objectID: String,
    val is_ready_made_product: Boolean,
    val price: Int
) {
    data class HighlightResult(
        val branch_id: BranchId,
        val category_name: CategoryName,
        val description: Description,
        val id: Id,
        val image: Image,
        val name: Name,
        val price: Price
    ) {
        data class BranchId(
            val matchLevel: String,
            val matchedWords: List<Any>,
            val value: String
        )

        data class CategoryName(
            val matchLevel: String,
            val matchedWords: List<Any>,
            val value: String
        )

        data class Description(
            val fullyHighlighted: Boolean,
            val matchLevel: String,
            val matchedWords: List<Any>,
            val value: String
        )

        data class Id(
            val matchLevel: String,
            val matchedWords: List<Any>,
            val value: String
        )

        data class Image(
            val matchLevel: String,
            val matchedWords: List<Any>,
            val value: String
        )

        data class Name(
            val matchLevel: String,
            val matchedWords: List<Any>,
            val value: String
        )

        data class Price(
            val matchLevel: String,
            val matchedWords: List<Any>,
            val value: String
        )
    }
}