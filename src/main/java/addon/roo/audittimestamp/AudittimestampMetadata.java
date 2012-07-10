package addon.roo.audittimestamp;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.springframework.roo.support.style.ToStringCreator;
import org.springframework.roo.support.util.Assert;

/**
 * This type produces metadata for a new ITD. It uses an {@link ItdTypeDetailsBuilder} provided by 
 * {@link AbstractItdTypeDetailsProvidingMetadataItem} to register a field in the ITD and a new method.
 * 
 * @since 1.1.0
 */
public class AudittimestampMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
	private static final String PROVIDES_TYPE_STRING = AudittimestampMetadata.class.getName();
	private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);

	private static final String AUDIT_CREATED_FIELD="created";
	private static final String AUDIT_UPDATED_FIELD="lastUpdated";
	
	public AudittimestampMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
		super(identifier, aspectName, governorPhysicalTypeMetadata);
		Assert.isTrue(isValid(identifier), "Metadata identification string '" + identifier + "' does not appear to be a valid");

		builder.addField(getAuditField(AUDIT_CREATED_FIELD,false));
		builder.addField(getAuditField(AUDIT_UPDATED_FIELD,true));
			
		
		builder.addMethod(getAuditMethod("auditCreatedTime",AUDIT_CREATED_FIELD,"javax.persistence.PrePersist"));
		builder.addMethod(getAuditMethod("auditUpdatedTime",AUDIT_UPDATED_FIELD,"javax.persistence.PreUpdate"));
		
		// Create a representation of the desired output ITD
		itdTypeDetails = builder.build();
	}
	
	private FieldMetadata getAuditField(String fieldName,boolean updateable) {
		
		List<AnnotationMetadataBuilder> list = new ArrayList<AnnotationMetadataBuilder>();
		AnnotationMetadataBuilder dateTimeFormat=new AnnotationMetadataBuilder(new JavaType("org.springframework.format.annotation.DateTimeFormat"));
		dateTimeFormat.addStringAttribute("style", "S-");
		
		AnnotationMetadataBuilder temporal=new AnnotationMetadataBuilder(new JavaType("javax.persistence.Temporal"));
		temporal.addEnumAttribute("value", "javax.persistence.TemporalType", "TIMESTAMP");
		
		AnnotationMetadataBuilder column=new AnnotationMetadataBuilder(new JavaType("javax.persistence.Column"));
		column.addBooleanAttribute("updatable", updateable);
		
		
		list.add(temporal);
		list.add(dateTimeFormat);
		list.add(column);
		
		// Using the FieldMetadataBuilder to create the field definition. 
		FieldMetadataBuilder fieldBuilder = new FieldMetadataBuilder(getId(), // Metadata ID provided by supertype
			Modifier.PRIVATE, // Using package protection rather than private
			list, // No annotations for this field
			new JavaSymbolName(fieldName), // Field name
			new JavaType("java.util.Date")); // Field type
		
		return fieldBuilder.build(); // Build and return a FieldMetadata instance
	}
	
	private MethodMetadata getAuditMethod(String methodNameStr,String field,String methodAnotation) {
		// Specify the desired method name
		JavaSymbolName methodName = new JavaSymbolName(methodNameStr);
		
		// Check if a method with the same signature already exists in the target type
		MethodMetadata method = methodExists(methodName, new ArrayList<AnnotatedJavaType>());
		if (method != null) {
			// If it already exists, just return the method and omit its generation via the ITD
			return method;
		}
		
		AnnotationMetadataBuilder prePersists=new AnnotationMetadataBuilder(new JavaType(methodAnotation));
		// Define method annotations (none in this case)
		List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
		annotations.add(prePersists);
		
		// Define method throws types (none in this case)
		List<JavaType> throwsTypes = new ArrayList<JavaType>();
		
		// Define method parameter types (none in this case)
		List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
		
		// Define method parameter names (none in this case)
		List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
		
		// Create the method body
		InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("this."+field+"=new java.util.Date();");
		
		// Use the MethodMetadataBuilder for easy creation of MethodMetadata
		MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC, methodName, JavaType.VOID_PRIMITIVE, parameterTypes, parameterNames, bodyBuilder);
		methodBuilder.setAnnotations(annotations);
		methodBuilder.setThrowsTypes(throwsTypes);
		
		return methodBuilder.build(); // Build and return a MethodMetadata instance
	}
	
	private MethodMetadata getSampleMethod() {
		// Specify the desired method name
		JavaSymbolName methodName = new JavaSymbolName("sampleMethod");
		
		// Check if a method with the same signature already exists in the target type
		MethodMetadata method = methodExists(methodName, new ArrayList<AnnotatedJavaType>());
		if (method != null) {
			// If it already exists, just return the method and omit its generation via the ITD
			return method;
		}
		
		// Define method annotations (none in this case)
		List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
		
		// Define method throws types (none in this case)
		List<JavaType> throwsTypes = new ArrayList<JavaType>();
		
		// Define method parameter types (none in this case)
		List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
		
		// Define method parameter names (none in this case)
		List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
		
		// Create the method body
		InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("System.out.println(\"Hello World\");");
		
		// Use the MethodMetadataBuilder for easy creation of MethodMetadata
		MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC, methodName, JavaType.VOID_PRIMITIVE, parameterTypes, parameterNames, bodyBuilder);
		methodBuilder.setAnnotations(annotations);
		methodBuilder.setThrowsTypes(throwsTypes);
		
		return methodBuilder.build(); // Build and return a MethodMetadata instance
	}
		
	private MethodMetadata methodExists(JavaSymbolName methodName, List<AnnotatedJavaType> paramTypes) {
		// We have no access to method parameter information, so we scan by name alone and treat any match as authoritative
		// We do not scan the superclass, as the caller is expected to know we'll only scan the current class
		for (MethodMetadata method : governorTypeDetails.getDeclaredMethods()) {
			if (method.getMethodName().equals(methodName) && method.getParameterTypes().equals(paramTypes)) {
				// Found a method of the expected name; we won't check method parameters though
				return method;
			}
		}
		return null;
	}
	
	// Typically, no changes are required beyond this point
	
	public String toString() {
		ToStringCreator tsc = new ToStringCreator(this);
		tsc.append("identifier", getId());
		tsc.append("valid", valid);
		tsc.append("aspectName", aspectName);
		tsc.append("destinationType", destination);
		tsc.append("governor", governorPhysicalTypeMetadata.getId());
		tsc.append("itdTypeDetails", itdTypeDetails);
		return tsc.toString();
	}

	public static final String getMetadataIdentiferType() {
		return PROVIDES_TYPE;
	}
	
	public static final String createIdentifier(JavaType javaType, Path path) {
		return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
	}

	public static final JavaType getJavaType(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	public static final Path getPath(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	public static boolean isValid(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}
}
