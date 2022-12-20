package br.buss.fincontrolapp.models;

public class Transaction {
    private Integer idTransaction;
    private Integer idUser;
    private Boolean isDebit;
    private Boolean isCredit;
    private String desc;
    private String time;
    private String typeDesc;
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public Integer getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(Integer idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Boolean getDebit() {
        return isDebit;
    }

    public void setDebit(Integer debit) {
        isDebit = debit == 1;
    }

    public Boolean getCredit() {
        return isCredit;
    }

    public void setCredit(Integer credit) {
        isCredit = credit == 1;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        String[] dateArray = time.split("-");
        this.time = dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0];
    }
}
