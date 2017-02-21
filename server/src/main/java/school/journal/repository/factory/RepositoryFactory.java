package school.journal.repository.factory;

import school.journal.entity.*;
import school.journal.repository.IRepository;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.impl.*;

public class RepositoryFactory {
    private static RepositoryFactory instance = new RepositoryFactory();
    private IRepository<Role> roleRepository = RoleRepository.getInstance();
    private IRepository<User> userIRepository = UserRepository.getInstance();
    private IRepository<Teacher> teacherIRepository = TeacherRepository.getInstance();
    private IRepository<TeacherM2MSubject> teacherM2MSubjectIRepository = TeacherM2MSubjectRepository.getInstance();
    private IRepository<SubjectInSchedule> subjectInScheduleIRepository = SubjectInScheduleRepository.getInstance();

    public static RepositoryFactory getInstance(){
        return instance;
    }

    public IRepository<Role> getRoleRepository(){
        return roleRepository;
    }

    public IRepository<User> getUserIRepository() {
        return userIRepository;
    }

    public IRepository<Teacher> getTeacherIRepository() {
        return teacherIRepository;
    }

    public IRepository<TeacherM2MSubject> getTeacherM2MSubjectIRepository() {
        return teacherM2MSubjectIRepository;
    }

    public IRepository<SubjectInSchedule> getSubjectInScheduleIRepository() {
        return subjectInScheduleIRepository;
    }
}
