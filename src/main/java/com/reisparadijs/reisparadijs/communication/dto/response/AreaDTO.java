package com.reisparadijs.reisparadijs.communication.dto.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 10:00
 */

public class AreaDTO {
    private int zipcodeNumbers;
    private final Logger logger = LoggerFactory.getLogger(AreaDTO.class);

    public AreaDTO(){};

    public AreaDTO(int zipcodeNumbers) {
        this.zipcodeNumbers = zipcodeNumbers;
    }

    public int getZipcodeNumbers() {
        return zipcodeNumbers;
    }

}
