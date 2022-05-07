package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;

@Entity
public class ApplicationUser {

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

    @Column(nullable = false, length = 64)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", referencedColumnName = "addressId", nullable = false)
    private Address address;

    @Override
    public String toString() {
        return "ApplicationUser{" + "userId=" + userId + ", email='" + email + '\''
            + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", gender="
            + gender + ", " + address + '\'' + ", password=" + password
            + ", hasAdministrativeRights=" + hasAdministrativeRights + ", loginTries=" + loginTries
            + ", mustResetPassword=" + mustResetPassword + ", lockedAccount=" + lockedAccount
            + ", articles=" + articles + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationUser user = (ApplicationUser) o;
        return userId == user.userId && hasAdministrativeRights == user.hasAdministrativeRights
            && loginTries == user.loginTries && mustResetPassword == user.mustResetPassword
            && lockedAccount == user.lockedAccount && Objects.equals(email, user.email)
            && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
            && gender == user.gender && Objects.equals(address, user.address) && Objects.equals(
            password, user.password) && Objects.equals(articles, user.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, firstName, lastName, gender, address,
            hasAdministrativeRights, loginTries, mustResetPassword, lockedAccount, articles,
            password);

    }

    @Column(nullable = false)
    private boolean hasAdministrativeRights;

    @Column(nullable = false)
    private long loginTries;

    @Column(nullable = false)
    private boolean mustResetPassword;

    @Column(nullable = false)
    private boolean lockedAccount;

    @ManyToMany
    @JoinTable(name = "ReadArticle", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "articleId"))
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
