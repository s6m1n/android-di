package woowacourse.shopping.di

import com.example.di.DependencyMappingModule
import com.example.di.Mapping
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.local.LocalCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

@DependencyMappingModule
abstract class RepositoryModule {
    @Mapping
    abstract fun provideDefaultProductRepository(productRepositoryImpl: DefaultProductRepository): ProductRepository

    @Mapping
    abstract fun provideInMemoryCartRepository(cartRepositoryImpl: InMemoryCartRepository): CartRepository

    @Mapping
    abstract fun provideLocalCartRepository(cartRepositoryImpl: LocalCartRepository): CartRepository
}
