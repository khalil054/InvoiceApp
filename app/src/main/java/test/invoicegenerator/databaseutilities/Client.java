package test.invoicegenerator.databaseutilities;

import java.io.Serializable;

public class Client implements Serializable {
    private int ClientID;
    private String ClientName;
    private String ClientPhone;
    private String ClientEmail;
    private String ClientAddress;
    private String ClientCity;
    private String ClientState;
    private String ClientCountry;
    private String ClientZipCode;


    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int clientID) {
        ClientID = clientID;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientPhone() {
        return ClientPhone;
    }

    public void setClientPhone(String clientPhone) {
        ClientPhone = clientPhone;
    }

    public String getClientEmail() {
        return ClientEmail;
    }

    public void setClientEmail(String clientEmail) {
        ClientEmail = clientEmail;
    }

    public String getClientAddress() {
        return ClientAddress;
    }

    public void setClientAddress(String clientAddress) {
        ClientAddress = clientAddress;
    }

    public String getClientCity() {
        return ClientCity;
    }

    public void setClientCity(String clientCity) {
        ClientCity = clientCity;
    }

    public String getClientState() {
        return ClientState;
    }

    public void setClientState(String clientState) {
        ClientState = clientState;
    }

    public String getClientCountry() {
        return ClientCountry;
    }

    public void setClientCountry(String clientCountry) {
        ClientCountry = clientCountry;
    }

    public String getClientZipCode() {
        return ClientZipCode;
    }

    public void setClientZipCode(String clientZipCode) {
        ClientZipCode = clientZipCode;
    }

public Client()
{

}
    public Client(int clientID, String clientName, String clientPhone, String clientEmail, String clientAddress, String clientCity, String clientState, String clientCountry, String clientZipCode) {
        ClientID = clientID;
        ClientName = clientName;
        ClientPhone = clientPhone;
        ClientEmail = clientEmail;
        ClientAddress = clientAddress;
        ClientCity = clientCity;
        ClientState = clientState;
        ClientCountry = clientCountry;
        ClientZipCode = clientZipCode;

        setClientID(ClientID);
        setClientName(ClientName);
        setClientPhone(ClientPhone);
        setClientEmail(ClientEmail);
        setClientAddress(ClientAddress);
        setClientCity(ClientCity);
        setClientState(clientState);
        setClientCountry(ClientCountry);
        setClientZipCode(clientZipCode);
    }

    public String getClinetObject(){

        return (getClientID() + "," +
                getClientName() + "," +
                getClientPhone() + "," +
                getClientEmail() + "," +
                getClientAddress()+ "," +
                getClientCity()+ "," +
                getClientState()+ "," +
                getClientCountry()+ "," +
                getClientZipCode()
        );
    }
}
