
package ca.toantrieu.dms.Repository;

import ca.toantrieu.dms.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String username);
    User findById(long id);
}