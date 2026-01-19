package ru.ugrasu.eljunior.ui.screens.schedule;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class ScheduleViewModel_Factory implements Factory<ScheduleViewModel> {
  private final Provider<CourseRepository> courseRepositoryProvider;

  public ScheduleViewModel_Factory(Provider<CourseRepository> courseRepositoryProvider) {
    this.courseRepositoryProvider = courseRepositoryProvider;
  }

  @Override
  public ScheduleViewModel get() {
    return newInstance(courseRepositoryProvider.get());
  }

  public static ScheduleViewModel_Factory create(
      Provider<CourseRepository> courseRepositoryProvider) {
    return new ScheduleViewModel_Factory(courseRepositoryProvider);
  }

  public static ScheduleViewModel newInstance(CourseRepository courseRepository) {
    return new ScheduleViewModel(courseRepository);
  }
}
