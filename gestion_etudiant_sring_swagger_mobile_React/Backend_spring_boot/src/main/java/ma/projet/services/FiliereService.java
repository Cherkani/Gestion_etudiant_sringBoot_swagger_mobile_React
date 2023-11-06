package ma.projet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.projet.IDao.IDao;
import ma.projet.entities.Filiere;
import ma.projet.repisitories.FiliereRepository;
@Service
public class FiliereService implements IDao<Filiere> {

	@Autowired
	private FiliereRepository repisitory;

	@Override
	public Filiere create(Filiere o) {
		return repisitory.save(o);
	}

	@Override
	public Filiere update(Filiere o) {
		return repisitory.save(o);
	}

	@Override
	public Boolean delete(Filiere o) {
		try {
			repisitory.delete(o);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Filiere findById(int id) {
		return repisitory.findById(id).orElse(null);
	}

	@Override
	public List<Filiere> findAll() {
		return repisitory.findAll();
	}

}
