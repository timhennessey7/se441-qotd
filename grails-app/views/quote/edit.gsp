<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'quote.label', default: 'Quote')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-quote" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="edit-quote" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.quote}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.quote}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.quote}" method="PUT">
                <g:hiddenField name="version" value="${this.quote?.version}" />
                <fieldset class="form">
				<div class='fieldcontain required'>
<label for='text'>Text<span class='required-indicator'>*</span></label>
<input type="text" name="text" value="${this.quote?.text}" required=""
</div>
                   <feature:enabled feature="ShowAttributions">
						<div class='fieldcontain'>
						<label for='attribution'>Attribution</label>
						<select name="attribution.id" id="attribution" >
						<option value="null"></option>
						<option value="1" >qotd.Attribution : 1</option>
						</select>
						</div>
				</feature:enabled>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
