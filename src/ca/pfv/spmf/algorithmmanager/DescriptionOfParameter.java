package ca.pfv.spmf.algorithmmanager;

import java.io.Serializable;

/* This file is copyright (c) 2008-2013 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This class is used to describe an algorithm's parameter.
 *
 * @author Philippe Fournier-Viger, 2016
 * @see DescriptionOfAlgorithm
 */
public class DescriptionOfParameter implements Serializable {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = 6680232387395745034L;

    /**
     * name of this parameter
     */
    public final String name;

    /**
     * example value for this parameter
     */
    public final String example;

    /**
     * type of parameter value
     */
    @SuppressWarnings("rawtypes")
    public final Class parameterType;

    /**
     * this parameter is optional or not?
     */
    public final boolean isOptional;

    /**
     * Optional predefined values for this parameter.
     */
    private final String[] predefinedValues;

    /**
     * Constructor for this parameter
     *
     * @param name          the name of the parameter (a string)
     * @param example       a string providing an example value that this parameter
     *                      could take
     * @param parameterType the type of this parameter (e.g. Integer.class,
     *                      Double.class, String.class...)
     */
    public DescriptionOfParameter(String name, String example, @SuppressWarnings("rawtypes") Class parameterType,
                                  boolean isOptional) {
        this.name = name;
        this.example = example;
        this.parameterType = parameterType;
        this.isOptional = isOptional;
        this.predefinedValues = null;
    }

    /**
     * Constructor for a parameter whose value should be selected from a fixed list.
     *
     * @param name             the name of this parameter
     * @param example          an example value
     * @param parameterType    the type of this parameter
     * @param isOptional       whether this parameter is optional
     * @param predefinedValues the allowed predefined values
     */
    public DescriptionOfParameter(String name, String example, @SuppressWarnings("rawtypes") Class parameterType,
                                  boolean isOptional, String[] predefinedValues) {
        this.name = name;
        this.example = example;
        this.parameterType = parameterType;
        this.isOptional = isOptional;
        this.predefinedValues = predefinedValues;
    }

    @Override
    /**
     * Obtain a String representation of this parameter description
     *
     * @return a String
     */
    public String toString() {
        return "[" + name + ", " + example + ", " + parameterType + ", isOptional = " + isOptional + " ]";
    }

    /**
     * Get the name of this parameter
     *
     * @return the parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the example
     *
     * @return the example
     */
    public String getExample() {
        return example;
    }

    /**
     * Get the parameter type
     *
     * @return a class representing the type of parameter
     */
    @SuppressWarnings("rawtypes")
    public Class getParameterType() {
        return parameterType;
    }

    /**
     * Get the predefined values for this parameter, if any.
     *
     * @return predefined values, or null if the parameter is free text
     */
    public String[] getPredefinedValues() {
        return predefinedValues;
    }

    /**
     * Check if this parameter has predefined values.
     *
     * @return true if predefined values are available
     */
    public boolean hasPredefinedValues() {
        return predefinedValues != null && predefinedValues.length > 0;
    }

    /**
     * Check if this parameter is optional
     *
     * @return true if optional. Otherwise, false.
     */
    public boolean isOptional() {
        return isOptional;
    }
}
