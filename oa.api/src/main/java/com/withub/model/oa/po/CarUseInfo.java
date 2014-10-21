package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "OA_CARUSEINFO")
public class CarUseInfo extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    @OneToOne(targetEntity = Car.class)
    @JoinColumn(name = "carId")
    private Car car;

    @OneToOne(targetEntity = Driver.class)
    @JoinColumn(name = "driverId")
    private Driver driver;

    @ManyToOne(targetEntity = CarUse.class)
    @JoinColumn(name = "carUseId")
    private CarUse carUse;

    //================================ 属性方法 ==========================================================

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public CarUse getCarUse() {
        return carUse;
    }

    public void setCarUse(CarUse carUse) {
        this.carUse = carUse;
    }

}

