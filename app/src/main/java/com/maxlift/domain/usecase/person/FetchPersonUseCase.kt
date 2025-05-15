package com.maxlift.domain.usecase.person

import com.maxlift.domain.model.Person
import com.maxlift.domain.repository.IMyRepository

class FetchPersonUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(id: Int): Person? {
        return myRepository.fetchPersonById(id)
    }
}