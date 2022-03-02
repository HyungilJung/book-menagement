package com.farmskin.bookmenagemant.domain

import com.farmskin.bookmenagemant.global.BaseTimeEntity
import javax.persistence.*

@Entity
class Book(

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val author: String,

    @Column(nullable = false)
    var rentStatus: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    var category: Category,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    val id: Long = 0

) : BaseTimeEntity() {

    fun updateCategory(category: Category) {
        this.category = category
    }

    fun updateStatus(rentStatus: String) {
        this.rentStatus = rentStatus
    }
}