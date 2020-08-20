package ca.toantrieu.dms.Service;

import ca.toantrieu.dms.Model.Role;
import ca.toantrieu.dms.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findById(long id){
        return roleRepository.findById(id);
    }

    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }
}
