package android.app.di

import android.app.ui.imagelist.ImageListViewModel
import android.app.ui.imageupload.ImageUploadViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ImageUploadViewModel::class)
    abstract fun bindImageUploadViewModel(viewModel: ImageUploadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageListViewModel::class)
    abstract fun bindImageListViewModel(viewModel: ImageListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}