package ca.toantrieu.dms.Repository;

import ca.toantrieu.dms.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findById(long id);
    Role findByRole(String role);
}