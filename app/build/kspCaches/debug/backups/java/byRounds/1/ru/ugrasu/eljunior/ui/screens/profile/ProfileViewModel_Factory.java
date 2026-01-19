package ru.ugrasu.eljunior.ui.screens.profile;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import ru.ugrasu.eljunior.data.repository.AuthRepository;
import ru.ugrasu.eljunior.data.repository.CourseRepository;

@ScopeMetadata
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<CourseRepository> courseRepositoryProvider;

  public ProfileViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<CourseRepository> courseRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.courseRepositoryProvider = courseRepositoryProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(authRepositoryProvider.get(), courseRepositoryProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<CourseRepository> courseRepositoryProvider) {
    return new ProfileViewModel_Factory(authRepositoryProvider, courseRepositoryProvider);
  }

  public static ProfileViewModel newInstance(AuthRepository authRepository,
      CourseRepository courseRepository) {
    return new ProfileViewModel(authRepository, courseRepository);
  }
}
