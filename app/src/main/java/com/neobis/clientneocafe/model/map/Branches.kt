package com.neobis.clientneocafe.model.map

data class Branches(
    val address: String,
    val id: Int,
    val image: String,
    val link_to_map: String,
    val name_of_shop: String,
    val phone_number: String,
    val schedule: Schedule,
    val workdays: List<Workday>
) {
    data class Schedule(
        val description: String,
        val title: String
    )

    data class Workday(
        val end_time: String,
        val start_time: String,
        val workday: Int
    )
}