package com.salesforce.data;

import com.salesforce.utilities.RandomStringGenerator;
import com.salesforce.utilities.Randomizer;

public class SalesForceAccount extends BaseDataBean{
    @Randomizer(length=8)
    public String Name;

    @Randomizer(dataset = "shipping.cities")
    public String ShippingCity;

    @Randomizer(prefix="800", length=7, type= RandomStringGenerator.RandomizerTypes.DIGITS_ONLY)
    public String Phone;

    @Randomizer(prefix="800", length = 7, type= RandomStringGenerator.RandomizerTypes.DIGITS_ONLY)
    public String Fax;

    @Randomizer(prefix="3432", length = 7, type= RandomStringGenerator.RandomizerTypes.DIGITS_ONLY)
    public String AccountNumber;

    @Randomizer(dataset = "lightning.platform.url")
    public String Website;

    /*@Randomizer(dataset = "env.baseurl")
    public String  baseURL;*/

}