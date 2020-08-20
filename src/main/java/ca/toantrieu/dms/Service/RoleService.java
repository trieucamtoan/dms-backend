package ca.toantrieu.dms.Service;

import ca.toantrieu.dms.Model.Role;

public interface RoleService {
    Role findById(long id);
    Role findByRole(String name);
}
