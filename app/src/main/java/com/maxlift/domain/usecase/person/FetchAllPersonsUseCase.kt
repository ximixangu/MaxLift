package com.maxlift.domain.usecase.person

import com.maxlift.domain.model.Person
import com.maxlift.domain.repository.IMyRepository

class FetchAllPersonsUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(): List<Person> {
        return myRepository.fetchAllPersons()
    }
}