package qotd

import grails.test.mixin.*
import spock.lang.*

@TestFor(AttributionController)
@Mock(Attribution)
class AttributionControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params["name"] = 'Anonymous'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.attributionList
            model.attributionCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.attribution!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def attribution = new Attribution()
            attribution.validate()
            controller.save(attribution)

        then:"The create view is rendered again with the correct model"
            model.attribution!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            attribution = new Attribution(params)

            controller.save(attribution)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/attribution/show/1'
            controller.flash.message != null
            Attribution.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def attribution = new Attribution(params)
            controller.show(attribution)

        then:"A model is populated containing the domain instance"
            model.attribution == attribution
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def attribution = new Attribution(params)
            controller.edit(attribution)

        then:"A model is populated containing the domain instance"
            model.attribution == attribution
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/attribution/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def attribution = new Attribution()
            attribution.validate()
            controller.update(attribution)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.attribution == attribution

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            attribution = new Attribution(params).save(flush: true)
            controller.update(attribution)

        then:"A redirect is issued to the show action"
            attribution != null
            response.redirectedUrl == "/attribution/show/$attribution.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/attribution/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def attribution = new Attribution(params).save(flush: true)

        then:"It exists"
            Attribution.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(attribution)

        then:"The instance is deleted"
            Attribution.count() == 0
            response.redirectedUrl == '/attribution/index'
            flash.message != null
    }
}
