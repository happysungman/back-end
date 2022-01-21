package com.backend.study.test

import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

fun <T> Mono<T>.testWithTransactional(transactionManager: ReactiveTransactionManager): StepVerifier.FirstStep<T> =
    StepVerifier.create(TransactionHelper.withRollback(TransactionalOperator.create(transactionManager), this))

fun <T> Flux<T>.testWithTransactional(transactionManager: ReactiveTransactionManager): StepVerifier.FirstStep<T> =
    StepVerifier.create(TransactionHelper.withRollback(TransactionalOperator.create(transactionManager), this))

class TransactionHelper {
    companion object {
        fun <T> withRollback(operator: TransactionalOperator, publisher: Mono<T>): Mono<T> = operator.execute {
            it.setRollbackOnly()
            publisher
        }.next()

        fun <T> withRollback(operator: TransactionalOperator, publisher: Flux<T>): Flux<T> = operator.execute {
            it.setRollbackOnly()
            publisher
        }

        fun <T> executeWithRollback(operator: TransactionalOperator, function: () -> Mono<T>): Mono<T> =
            operator.execute {
                it.setRollbackOnly()
                function.invoke()
            }.next()

        fun <T> executeWithRollback(operator: TransactionalOperator, function: () -> Flux<T>): Flux<T> =
            operator.execute {
                it.setRollbackOnly()
                function.invoke()
            }
    }
}
