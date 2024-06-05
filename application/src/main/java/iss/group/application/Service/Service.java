package iss.group.application.Service;

import iss.group.application.Domain.*;
import iss.group.application.Repository.IRepositoryBilet;
import iss.group.application.Repository.IRepositoryLoc;
import iss.group.application.Repository.IRepositorySpectacol;
import iss.group.application.Repository.IRepositoryUsers;
import iss.group.application.Service.Utils.Observable;
import iss.group.application.Service.Utils.Observer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Service implements Observable {
    private final List<Observer> observers = new ArrayList<>();
    private IRepositorySpectacol spectacol;
    private IRepositoryLoc loc;
    private IRepositoryBilet bilet;
    private IRepositoryUsers users;


    public Service(IRepositoryUsers iRepositoryUsers, IRepositoryBilet iRepositoryBilet, IRepositoryLoc iRepositoryLoc, IRepositorySpectacol iRepositorySpectacol) {
        loc = iRepositoryLoc;
        bilet = iRepositoryBilet;
        users = iRepositoryUsers;
        spectacol = iRepositorySpectacol;
    }

    public List<Bilet> getAllBilete(){
        return (List<Bilet>) bilet.findAll();
    }

    public Loc findLoc(int id){
        return loc.findById(id);
    }

    public List<Loc> getAllLocuri(){
        return (List<Loc>) loc.findAll();
    }

    public User getUserByCredentials(String username, String password){
        return users.findByCredentials(username, password);
    }

    public void addUser(String username, String password, String name, String email){
        User user = new User(0, username, password, name, email, Type.CLIENT);
        users.save(user);
    }

    public List<Spectacol> getSpectacole(){
        return (List<Spectacol>) spectacol.findAll();
    }

    public void addSpectacole(String title, String description, Date data, float pret) throws Exception {
        for(Spectacol s : getSpectacole()){
            if (s.getData().getDay() == data.getDay() && s.getData().getMonth() == data.getMonth() && s.getData().getYear() == data.getYear())
            {
                throw new Exception("Date occupied");
            }
        }
        spectacol.save(new Spectacol(0, title, description, data, pret));
        notifyObservers();
    }

    public void updateSpectacole(Integer id, String title, String description, Date data, float pret){
        var exista = spectacol.findById(id);
        if (exista == null) return;
        spectacol.update(new Spectacol(id, title, description, data, pret));
        notifyObservers();
    }

    public void removeSpectacole(Integer id){
        var exista = spectacol.findById(id);
        if (exista == null) return;
        for(Bilet bilete : getAllBilete()){
            if (bilete.getSpectacol().getId() == id){
                bilet.delete(bilete.getId());
            }
        }
        spectacol.delete(id);
        notifyObservers();
    }

    public void addBilet(User user, Spectacol spectacol, Loc loc){
        bilet.save(new Bilet(0, user, loc, spectacol));
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
