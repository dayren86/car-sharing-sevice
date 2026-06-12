package mate.carsharing.conroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.rental.RentalCreteDto;
import mate.carsharing.dto.rental.RentalDto;
import mate.carsharing.model.User;
import mate.carsharing.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rental Api")
@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @Operation(summary = "Create new rental")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'CUSTOMER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalDto createNewRental(@AuthenticationPrincipal User user,
                                     @RequestBody @Valid RentalCreteDto rentalCreteDto) {
        return rentalService.createNewRental(user, rentalCreteDto);
    }

    @Operation(summary = "Show rentals detail information by user")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'CUSTOMER')")
    @GetMapping
    public List<RentalDto> getRentalSpecific(@AuthenticationPrincipal User user) {
        return rentalService.getAllRentalsByUser(user);
    }

    @Operation(summary = "Set actual return date")
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/{id}/return")
    @ResponseStatus(HttpStatus.OK)
    public RentalDto setActualReturnDate(@PathVariable Long id) {
        return rentalService.setReturnDate(id);
    }
}
