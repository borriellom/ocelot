/*
 * Copyright (C) 2015, VistaTEC or third-party contributors as indicated
 * by the @author tags or express copyright attribution statements applied by
 * the authors. All third-party contributions are distributed under license by
 * VistaTEC.
 *
 * This file is part of Ocelot.
 *
 * Ocelot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ocelot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, write to:
 *
 *     Free Software Foundation, Inc.
 *     51 Franklin Street, Fifth Floor
 *     Boston, MA 02110-1301
 *     USA
 *
 * Also, see the full LGPL text here: <http://www.gnu.org/copyleft/lesser.html>
 */
package com.vistatec.ocelot.config.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * Provenance configuration XML element.
 */
public class ProvenanceConfig {
    protected String revPerson, revOrganization, externalReference;

    @XmlElement
    public String getRevPerson() {
        return revPerson;
    }

    public void setRevPerson(String revPerson) {
        this.revPerson = revPerson;
    }

    @XmlElement
    public String getRevOrganization() {
        return revOrganization;
    }

    public void setRevOrganization(String revOrganization) {
        this.revOrganization = revOrganization;
    }

    @XmlElement
    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }
}