package ma.projet.services;

import ma.projet.IDao.IDao;
import ma.projet.entities.Role;
import ma.projet.repisitories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService implements IDao<Role> {
    @Autowired
    RoleRepository repisitory;


    @Override
    public Role create(Role o) {
      return repisitory.save(o);
    }

    @Override
    public Role update(Role o) {
        return repisitory.save(o);
    }

    @Override
    public Boolean delete(Role o) {
        try {
            repisitory.delete(o);
            return true;
        }catch(Exception e){
            return  false;
        }
    }

    @Override
    public Role findById(int id) {
        return repisitory.findById(id).orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return repisitory.findAll();
    }
}
