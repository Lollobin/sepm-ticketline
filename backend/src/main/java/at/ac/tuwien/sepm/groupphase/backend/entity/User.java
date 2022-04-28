package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.Type;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 16)
    private String zipCode;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 64)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] password;

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", gender=" + gender +
            ", street='" + street + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            ", password=" + Arrays.toString(password) +
            ", salt=" + Arrays.toString(salt) +
            ", hasAdministrativeRights=" + hasAdministrativeRights +
            ", loginTries=" + loginTries +
            ", mustResetPassword=" + mustResetPassword +
            ", lockedAccount=" + lockedAccount +
            ", articles=" + articles +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return userId == user.userId && hasAdministrativeRights == user.hasAdministrativeRights
            && loginTries == user.loginTries && mustResetPassword == user.mustResetPassword
            && lockedAccount == user.lockedAccount && Objects.equals(email, user.email)
            && Objects.equals(firstName, user.firstName) && Objects.equals(lastName,
            user.lastName) && gender == user.gender && Objects.equals(street, user.street)
            && Objects.equals(zipCode, user.zipCode) && Objects.equals(city,
            user.city) && Objects.equals(country, user.country) && Arrays.equals(
            password, user.password) && Arrays.equals(salt, user.salt)
            && Objects.equals(articles, user.articles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userId, email, firstName, lastName, gender, street, zipCode, city,
            country, hasAdministrativeRights, loginTries, mustResetPassword, lockedAccount,
            articles);
        result = 31 * result + Arrays.hashCode(password);
        result = 31 * result + Arrays.hashCode(salt);
        return result;
    }

    @Column(nullable = false, length = 16)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] salt;

    @Column(nullable = false)
    private boolean hasAdministrativeRights;

    @Column(nullable = false)
    private long loginTries;

    @Column(nullable = false)
    private boolean mustResetPassword;

    @Column(nullable = false)
    private boolean lockedAccount;

    @ManyToMany
    @JoinTable(
        name = "ReadArticle",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "articleId")
    )
    private Set<Article> articles;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public boolean isHasAdministrativeRights() {
        return hasAdministrativeRights;
    }

    public void setHasAdministrativeRights(boolean hasAdministrativeRights) {
        this.hasAdministrativeRights = hasAdministrativeRights;
    }

    public long getLoginTries() {
        return loginTries;
    }

    public void setLoginTries(long loginTries) {
        this.loginTries = loginTries;
    }

    public boolean isMustResetPassword() {
        return mustResetPassword;
    }

    public void setMustResetPassword(boolean mustResetPassword) {
        this.mustResetPassword = mustResetPassword;
    }

    public boolean isLockedAccount() {
        return lockedAccount;
    }

    public void setLockedAccount(boolean lockedAccount) {
        this.lockedAccount = lockedAccount;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
}
