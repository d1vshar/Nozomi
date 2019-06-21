package io.github.adorableskullmaster.nozomi.core.database.models.modules;

public class BankModule {
  private String apiKey;
  private Long bankChannel;
  private Resources resources;

  public BankModule() {
  }

  public BankModule(String apiKey, Long bankChannel, Resources resources) {
    this.apiKey = apiKey;
    this.bankChannel = bankChannel;
    this.resources = resources;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public Resources getResources() {
    return resources;
  }

  public void setResources(Resources resources) {
    this.resources = resources;
  }

  public Long getBankChannel() {
    return bankChannel;
  }

  public void setBankChannel(Long bankChannel) {
    this.bankChannel = bankChannel;
  }

  public class Resources {
    private Integer money = 100000;
    private Integer coal = 1000;
    private Integer oil = 1000;
    private Integer uranium = 1000;
    private Integer lead = 1000;
    private Integer iron = 1000;
    private Integer bauxite = 1000;
    private Integer gasoline = 1000;
    private Integer munitions = 1000;
    private Integer steel = 1000;
    private Integer aluminum = 1000;
    private Integer food = 1000;

    public Resources() {
    }

    public Resources(Integer money, Integer coal, Integer oil, Integer uranium, Integer lead, Integer iron, Integer bauxite, Integer gasoline, Integer munitions, Integer steel, Integer aluminum, Integer food) {
      this.money = money;
      this.coal = coal;
      this.oil = oil;
      this.uranium = uranium;
      this.lead = lead;
      this.iron = iron;
      this.bauxite = bauxite;
      this.gasoline = gasoline;
      this.munitions = munitions;
      this.steel = steel;
      this.aluminum = aluminum;
      this.food = food;
    }

    public Integer getMoney() {
      return money;
    }

    public void setMoney(Integer money) {
      this.money = money;
    }

    public Integer getCoal() {
      return coal;
    }

    public void setCoal(Integer coal) {
      this.coal = coal;
    }

    public Integer getOil() {
      return oil;
    }

    public void setOil(Integer oil) {
      this.oil = oil;
    }

    public Integer getUranium() {
      return uranium;
    }

    public void setUranium(Integer uranium) {
      this.uranium = uranium;
    }

    public Integer getLead() {
      return lead;
    }

    public void setLead(Integer lead) {
      this.lead = lead;
    }

    public Integer getIron() {
      return iron;
    }

    public void setIron(Integer iron) {
      this.iron = iron;
    }

    public Integer getBauxite() {
      return bauxite;
    }

    public void setBauxite(Integer bauxite) {
      this.bauxite = bauxite;
    }

    public Integer getGasoline() {
      return gasoline;
    }

    public void setGasoline(Integer gasoline) {
      this.gasoline = gasoline;
    }

    public Integer getMunitions() {
      return munitions;
    }

    public void setMunitions(Integer munitions) {
      this.munitions = munitions;
    }

    public Integer getSteel() {
      return steel;
    }

    public void setSteel(Integer steel) {
      this.steel = steel;
    }

    public Integer getAluminum() {
      return aluminum;
    }

    public void setAluminum(Integer aluminum) {
      this.aluminum = aluminum;
    }

    public Integer getFood() {
      return food;
    }

    public void setFood(Integer food) {
      this.food = food;
    }
  }


}
