//
// Used in order to have the desired namespace and prefix for the service's responses.
//

@XmlSchema(
   xmlns = @XmlNs(prefix = "sas", namespaceURI = "http://www.enginizer.com/services/sas/v1"),
   namespace = "http://www.enginizer.com/services/sas/v1",
   elementFormDefault = XmlNsForm.QUALIFIED)

package com.enginizer.services.sas;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;


