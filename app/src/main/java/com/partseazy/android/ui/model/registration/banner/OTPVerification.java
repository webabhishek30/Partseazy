package com.partseazy.android.ui.model.registration.banner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 12/12/16.
 */

public class OTPVerification {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("mobile_verified")
    @Expose
    private Integer mobileVerified;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("email_verified")
    @Expose
    private Integer emailVerified;
    @SerializedName("roles")
    @Expose
    private List<String> roles = null;
    @SerializedName("isActive")
    @Expose
    private Integer active;
    @SerializedName("info")
    @Expose
    private Object info;
    @SerializedName("tags")
    @Expose
    private Object tags;
    @SerializedName("machine_state")
    @Expose
    private Object machineState;
    @SerializedName("stated_at")
    @Expose
    private Object statedAt;
    @SerializedName("deleted")
    @Expose
    private Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public Object getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(Object name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     *
     * @param mobile
     * The mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     *
     * @return
     * The mobileVerified
     */
    public Integer getMobileVerified() {
        return mobileVerified;
    }

    /**
     *
     * @param mobileVerified
     * The mobile_verified
     */
    public void setMobileVerified(Integer mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    /**
     *
     * @return
     * The email
     */
    public Object getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(Object email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The emailVerified
     */
    public Integer getEmailVerified() {
        return emailVerified;
    }

    /**
     *
     * @param emailVerified
     * The email_verified
     */
    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     *
     * @return
     * The roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     *
     * @param roles
     * The roles
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     *
     * @return
     * The isActive
     */
    public Integer getActive() {
        return active;
    }

    /**
     *
     * @param active
     * The isActive
     */
    public void setActive(Integer active) {
        this.active = active;
    }

    /**
     *
     * @return
     * The info
     */
    public Object getInfo() {
        return info;
    }

    /**
     *
     * @param info
     * The info
     */
    public void setInfo(Object info) {
        this.info = info;
    }

    /**
     *
     * @return
     * The tags
     */
    public Object getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(Object tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The machineState
     */
    public Object getMachineState() {
        return machineState;
    }

    /**
     *
     * @param machineState
     * The machine_state
     */
    public void setMachineState(Object machineState) {
        this.machineState = machineState;
    }

    /**
     *
     * @return
     * The statedAt
     */
    public Object getStatedAt() {
        return statedAt;
    }

    /**
     *
     * @param statedAt
     * The stated_at
     */
    public void setStatedAt(Object statedAt) {
        this.statedAt = statedAt;
    }

    /**
     *
     * @return
     * The deleted
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     *
     * @param deleted
     * The deleted
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     *
     * @return
     * The deletedAt
     */
    public Object getDeletedAt() {
        return deletedAt;
    }

    /**
     *
     * @param deletedAt
     * The deleted_at
     */
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}

