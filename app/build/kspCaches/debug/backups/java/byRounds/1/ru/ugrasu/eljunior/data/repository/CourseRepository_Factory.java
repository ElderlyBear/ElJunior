package ru.ugrasu.eljunior.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class CourseRepository_Factory implements Factory<CourseRepository> {
  private final Provider<MoodleApi> moodleApiProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public CourseRepository_Factory(Provider<MoodleApi> moodleApiProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.moodleApiProvider = moodleApiProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public CourseRepository get() {
    return newInstance(moodleApiProvider.get(), authRepositoryProvider.get());
  }

  public static CourseRepository_Factory create(Provider<MoodleApi> moodleApiProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new CourseRepository_Factory(moodleApiProvider, authRepositoryProvider);
  }

  public static CourseRepository newInstance(MoodleApi moodleApi, AuthRepository authRepository) {
    return new CourseRepository(moodleApi, authRepository);
  }
}
