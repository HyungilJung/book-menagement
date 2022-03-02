package com.farmskin.bookmenagemant.domain

import com.farmskin.bookmenagemant.global.BaseTimeEntity
import javax.persistence.*

@Entity
class Category(

    @Column(nullable = false)
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    val id: Int = 0,

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    val books: Set<Book> = LinkedHashSet()

) : BaseTimeEntity()