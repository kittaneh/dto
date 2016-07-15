package org.grails.plugins.dto

import grails.core.GrailsApplication
import grails.dev.commands.*
import org.grails.build.parsing.CommandLine

/**
 * Created by yahya on 5/17/16.
 */
class GenerateDtoCommand implements ApplicationCommand {

    @Override
    boolean handle(ExecutionContext ctx) {

        CommandLine commandLine = ctx.commandLine
        def replacePackage = "*"
        def targetPackage = null

        // Check that the "dto.package.transforms" setting is the correct type.
//        if (!buildConfig.dto.package.transform instanceof Map) {
//            println "The 'dto.package.transform' build setting must be a map."
//            exit(1)
//        }

        if(!commandLine.hasOption("domain")){
            println "Please set the -domain option "
            return false
        }

//        if (commandLine.hasOption("pkg")) {
//            // --pkg cannot be used with either --oldpkg or --newpkg.
//            if (commandLine.hasOption("oldpkg") || commandLine.hasOption("newpkg")) {
//                println "You cannot use --pkg with either --oldpkg or --newpkg"
//                return false
//            }
//
//            // Use the specified package.
//            targetPackage = commandLine.optionValue("pkg")
//
//        } else if (commandLine.hasOption("oldpkg") || commandLine.hasOption("newPkg")) {
//            if (!(commandLine.hasOption("oldpkg") && commandLine.hasOption("newpkg"))) {
//                println "You must specify both --oldpkg and --newpkg, not just one of them"
//                return false
//            }
//
//            // Use the specified packages.
//            replacePackage = commandLine.optionValue("oldpkg")
//            targetPackage = commandLine.optionValue("newpkg")
//        }
//
//        def domainClasses
//        if (commandLine.hasOption("all")) {
//            def response = confirmInput("Are you sure you want to generate DTOs for all domain classes?")
//            if (!response) {
//                return 0
//            }
//            else {
//
//                 GrailsApplication  application  = (GrailsApplication) applicationContext.getBean("grailsApplication")
//                 domainClasses = application.domainClasses
//            }
//        }
//        else {
//            promptForName(type: "Domain class")
//
//            def failed = false
//            def params = argsMap["params"]
//            domainClasses = params.collect {
//                def dc = grailsApp.getDomainClass(it)
//                if (!dc) {
//                    failed = true;
//                    event("StatusError", [ "Cannot find domain class $it" ])
//                }
//                return dc
//            }
//
//            if (failed) return false
//        }


        return false
    }
}
