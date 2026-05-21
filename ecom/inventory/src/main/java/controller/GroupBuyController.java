package controller;

import dto.request.CreateGroupBuyRequest;
import entity.GroupBuy;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import service.GroupBuyService;

import java.time.LocalDateTime;

@Controller("/api/group-buys")
@Secured(SecurityRule.IS_ANONYMOUS)
public class GroupBuyController {

    private final GroupBuyService groupBuyService;

    public GroupBuyController(GroupBuyService groupBuyService) {
        this.groupBuyService = groupBuyService;
    }

    // CREATE group buy
    @Post
    public HttpResponse<GroupBuy> create(@Body CreateGroupBuyRequest request) {

        GroupBuy gb = groupBuyService.createGroupBuy(
                request.getProductId(),
                request.getTargetQuantity(),
                request.getEndDate()
        );

        return HttpResponse.created(gb);
    }

    // JOIN group buy
    @Post("/{id}/join")
    public HttpResponse<String> join(@PathVariable String id,
                                     @QueryValue int quantity) {

        groupBuyService.joinGroupBuy(id, quantity);

        return HttpResponse.ok("Joined successfully");
    }

    // GET one group buy
    @Get("/{id}")
    public HttpResponse<GroupBuy> get(@PathVariable String id) {
        return HttpResponse.ok(groupBuyService.getById(id));
    }

    // CLOSE expired manually (optional admin/debug)
    @Post("/{id}/close")
    public HttpResponse<String> close(@PathVariable String id) {
        groupBuyService.closeGroupBuy(id);
        return HttpResponse.ok("Closed");
    }

    @Get("/product/{productId}")
    public HttpResponse<GroupBuy> getByProduct(@PathVariable String productId) {

        return groupBuyService
                .getByProductIdAndStatus(productId, "OPEN")
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }
}
