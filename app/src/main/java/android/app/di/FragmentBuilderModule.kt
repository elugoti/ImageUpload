package android.app.di

import android.app.ui.HomeFragment
import android.app.ui.imagelist.ImageListFragment
import android.app.ui.imageupload.CameraFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeCameraFragment(): CameraFragment

    @ContributesAndroidInjector
    abstract fun contributeImageListFragment(): ImageListFragment

}