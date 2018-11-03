package com.ty.digitalfarms.bean;

import java.util.List;

/**
 * description:
 * author: XingZheng
 * date: 2016/11/14.
 */
public class GXLoginInfo {

    private int code;
    private String msg;
    private List<List<String>> functions;
    private List<List<String>> menus;
    private List<List<String>> roles;
    private List<String> users;

    @Override
    public String toString() {
        return "GXLoginInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", functions=" + functions +
                ", menus=" + menus +
                ", roles=" + roles +
                ", users=" + users +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<List<String>> getFunctions() {
        return functions;
    }

    public void setFunctions(List<List<String>> functions) {
        this.functions = functions;
    }

    public List<List<String>> getMenus() {
        return menus;
    }

    public void setMenus(List<List<String>> menus) {
        this.menus = menus;
    }

    public List<List<String>> getRoles() {
        return roles;
    }

    public void setRoles(List<List<String>> roles) {
        this.roles = roles;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
