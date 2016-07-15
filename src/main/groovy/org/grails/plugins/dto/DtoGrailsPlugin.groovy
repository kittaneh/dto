package org.grails.plugins.dto

import grails.plugins.*
import org.dozer.spring.DozerBeanMapperFactoryBean
import org.springframework.context.ApplicationContext

class DtoGrailsPlugin extends Plugin {


    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.4 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // the other plugins this plugin depends on
    def dependsOn = [:]
    def loadAfter = [ "converters" ]

    // TODO Fill in these fields
    def title = "Dto" // Headline display name of the plugin
    def author = "Yahya Dawoud"
    def authorEmail = "kittaneh@gmail.com"
    def description = '''\
This is a conversion for the grails DTO plugin to Grails 3.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/dto"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    Closure doWithSpring() {
        { ->
            // Create the DTO generator bean.
            if (application.config.grails.generate.indent) {
                dtoGenerator(DefaultGrailsDtoGenerator, true, application.config.grails.generate.indent)
            } else {
                dtoGenerator(DefaultGrailsDtoGenerator)
            }

            dozerMapper(DozerBeanMapperFactoryBean) {
                if (application.config.dto.mapping.files) {
                    mappingFiles = application.config.dto.mapping.files
                }
            }
        }
    }

    void doWithDynamicMethods() {
        // TODO Implement registering dynamic methods to classes (optional)
        // Add "as DTO" and toDTO() to domain classes.
        for (dc in grailsApplication.domainClasses) {
            addDtoMethods(dc.metaClass, applicationContext)
        }

        // Add the toDTO(Class) method to collections.
        Collection.metaClass.toDTO = { obj ->
            // Find out what class the target collection should contain.
            def containedClass = obj instanceof Class ? obj : obj.getClass()

            // Next create a collection of the appropriate type.
            def clazz = delegate.getClass()
            if (SortedSet.isAssignableFrom(clazz)) {
                obj = new TreeSet()
            }
            else if (Set.isAssignableFrom(clazz)) {
                obj = new HashSet()
            }
            else {
                obj = new ArrayList(delegate.size())
            }

            // Finally, add the individual DTOs to the new collection.
            final mapper = applicationContext.getBean("dozerMapper")
            delegate.each { obj << mapper.map(it, containedClass) }
            return obj
        }
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }

    private addDtoMethods(final MetaClass mc, final ApplicationContext ctx) {
        // First add the "as DTO".
        final originalAsType = mc.getMetaMethod("asType", [ Class ] as Object[])
        mc.asType = { Class clazz ->
            if (DTO == clazz) {
                // Do the DTO conversion.
                return mapDomainInstance(ctx, delegate)
            }
            else {
                // Use the original asType implementation.
                return originalAsType.invoke(delegate, [ clazz ] as Object[])
            }
        }

        // Then the toDTO() method.
        mc.toDTO = {->
            return mapDomainInstance(ctx, delegate)
        }

        mc.toDTO = { Class clazz ->
            // Convert the domain instance to a DTO.
            def mapper = ctx.getBean("dozerMapper")
            return mapper.map(delegate, clazz)
        }
    }

    /**
     * Uses the Dozer mapper to map a domain instance to its corresponding
     * DTO.
     * @param ctx The Spring application context containing the Dozer
     * mapper.
     * @param obj The domain instance to map.
     * @return The DTO corresponding to the given domain instance.
     */
    private mapDomainInstance(ctx, obj) {
        // Get the appropriate DTO class for this domain instance.
        def dtoClassName = obj.getClass().name + "DTO"
        def dtoClass = obj.getClass().classLoader.loadClass(dtoClassName)

        // Now convert the domain instance to a DTO.
        def mapper = ctx.getBean("dozerMapper")
        return mapper.map(obj, dtoClass)
    }
}
