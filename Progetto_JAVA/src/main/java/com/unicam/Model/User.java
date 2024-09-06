package com.unicam.Model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "utente")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "utente",
            sequenceName = "utente_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "utente"
    )
    private long id;
    private String name;
    private String username;
    private String comune;
    private String comuneVisitato;
    private String email;
    private String password;
    protected Ruolo ruoloComune;

    public User(String name, String email, String password, String comune, String username) {
        this.name = name;
        this.email = email;
        this.comune = comune;
        this.username = username;
        this.password = password;
    }

    //mapper using
    public User(){}

    public void CriptaPassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(this.password);
    }

    public long getId() {
        return id;
    }

    /**
     * Questo metodo restituisce il nome dell'utente.
     *
     * @return name.
     */

    public String getName() {
        return name;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param name dell'utente.
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Questo metodo restituisce l'username dell'utente.
     *
     * @return username.
     */
    public String getUsername(){
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /**
     * Imposta lo username dell'utente.
     *
     * @param username dell'utente.
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Questo metodo restituisce il comune di residenza dell'utente.
     *
     * @return comune.
     */
    public String getComune(){
        return comune;
    }

    /**
     * Imposta il comune dell'utente.
     *
     * @param comune dell'utente.
     */
    public void setComune(String comune) {
        this.comune = comune;
    }

    /**
     * Questo metodo restituisce l'email dell'utente.
     *
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email dell'utente.
     */
    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Questo metodo restituisce la password dell'utente.
     *
     * @return password.
     */
    public String getPassword() {
        return password;
    }


    /**
     * Questo metodo restituisce il ruolo dell'utente.
     *
     * @return ruolo.
     */
    public Ruolo getRuoloComune(){
        return ruoloComune;
    }

    /**
     * Imposta il ruolo dell'utente.
     *
     * @param ruoloComune dell'utente.
     */
    public void setRuoloComune(Ruolo ruoloComune){
        this.ruoloComune = ruoloComune;
    }

    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Restituisce una rappresentazione in formato stringa dell'oggetto User.
     * Questa rappresentazione include il nome, l'email e il comune dell'utente.
     *
     * @return Una stringa che rappresenta l'oggetto User.
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", comune='" + comune + '\'' +
                ", email='" + email + '\'' +
                ", ruolo=" + ruoloComune +
                '}';
    }


    /**
     * Confronta questo oggetto User con l'oggetto specificato per verificarne l'uguaglianza.
     * Due oggetti User sono considerati uguali se hanno lo stesso nome, email e comune.
     *
     * @param o L'oggetto da confrontare con questo User.
     * @return true se gli oggetti sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) && username.equals(user.username);
    }

    /**
     * Restituisce un codice hash per l'oggetto User.
     * Questo codice hash è calcolato utilizzando il nome, l'email e il comune dell'utente.
     *
     * @return Il codice hash per questo oggetto User.
     */
    @Override
    public int hashCode() {
        return Objects.hash(email,username);
    }

    public String getComuneVisitato() {
        return comuneVisitato;
    }

    public void setComuneVisitato(String comuneVisitato) {
        this.comuneVisitato = comuneVisitato;
    }
}