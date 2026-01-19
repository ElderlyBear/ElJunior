package ru.ugrasu.eljunior.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;
import ru.ugrasu.eljunior.data.api.MoodleApi;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AppModule_ProvideMoodleApiFactory implements Factory<MoodleApi> {
  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvideMoodleApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public MoodleApi get() {
    return provideMoodleApi(retrofitProvider.get());
  }

  public static AppModule_ProvideMoodleApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvideMoodleApiFactory(retrofitProvider);
  }

  public static MoodleApi provideMoodleApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMoodleApi(retrofit));
  }
}
