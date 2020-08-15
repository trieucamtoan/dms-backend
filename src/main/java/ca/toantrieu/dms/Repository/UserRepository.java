
package ca.toantrieu.dms.Repository;

import ca.toantrieu.dms.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserName(String username);
}