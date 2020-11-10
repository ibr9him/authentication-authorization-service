package com.estore.authenticationauthorizationservice.role;

import com.estore.authenticationauthorizationservice.activity.ActivityEntity;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityTagNameDto;
import com.estore.authenticationauthorizationservice.activity.tagname.TagNameEntity;
import com.estore.authenticationauthorizationservice.bundle.BundleEntity;
import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto;
import com.estore.authenticationauthorizationservice.client.ClientEntity;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
import com.estore.authenticationauthorizationservice.role.authority.Authority;
import com.estore.authenticationauthorizationservice.role.dto.RoleCreationDto;
import com.estore.authenticationauthorizationservice.role.dto.RoleDto;
import com.estore.authenticationauthorizationservice.role.dto.RoleDto.RoleDtoBuilder;
import com.estore.authenticationauthorizationservice.role.dto.RoleUpdatingDto;
import com.estore.authenticationauthorizationservice.subscription.SubscriptionEntity;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-10T23:46:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.5 (JetBrains s.r.o)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleEntity toEntity(RoleCreationDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setName( userDto.getName() );
        roleEntity.setNameAr( userDto.getNameAr() );
        roleEntity.setCode( userDto.getCode() );
        roleEntity.setClient( clientDtoToClientEntity( userDto.getClient() ) );
        Set<Authority> set = userDto.getAuthorities();
        if ( set != null ) {
            roleEntity.setAuthorities( new HashSet<Authority>( set ) );
        }

        return roleEntity;
    }

    @Override
    public RoleEntity toEntity(RoleUpdatingDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setId( userDto.getId() );
        roleEntity.setName( userDto.getName() );
        roleEntity.setNameAr( userDto.getNameAr() );
        roleEntity.setCode( userDto.getCode() );
        Set<Authority> set = userDto.getAuthorities();
        if ( set != null ) {
            roleEntity.setAuthorities( new HashSet<Authority>( set ) );
        }

        return roleEntity;
    }

    @Override
    public RoleEntity toEntity(RoleDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setId( userDto.getId() );
        roleEntity.setName( userDto.getName() );
        roleEntity.setNameAr( userDto.getNameAr() );
        roleEntity.setCode( userDto.getCode() );
        Set<Authority> set = userDto.getAuthorities();
        if ( set != null ) {
            roleEntity.setAuthorities( new HashSet<Authority>( set ) );
        }

        return roleEntity;
    }

    @Override
    public RoleDto toRoleDto(RoleEntity user) {
        if ( user == null ) {
            return null;
        }

        RoleDtoBuilder roleDto = RoleDto.builder();

        roleDto.id( user.getId() );
        roleDto.name( user.getName() );
        roleDto.nameAr( user.getNameAr() );
        roleDto.code( user.getCode() );
        Set<Authority> set = user.getAuthorities();
        if ( set != null ) {
            roleDto.authorities( new HashSet<Authority>( set ) );
        }

        return roleDto.build();
    }

    protected TagNameEntity activityTagNameDtoToTagNameEntity(ActivityTagNameDto activityTagNameDto) {
        if ( activityTagNameDto == null ) {
            return null;
        }

        TagNameEntity tagNameEntity = new TagNameEntity();

        tagNameEntity.setKey( activityTagNameDto.getKey() );
        tagNameEntity.setLocale( activityTagNameDto.getLocale() );
        tagNameEntity.setMessage( activityTagNameDto.getMessage() );

        return tagNameEntity;
    }

    protected Set<TagNameEntity> activityTagNameDtoSetToTagNameEntitySet(Set<ActivityTagNameDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<TagNameEntity> set1 = new HashSet<TagNameEntity>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ActivityTagNameDto activityTagNameDto : set ) {
            set1.add( activityTagNameDtoToTagNameEntity( activityTagNameDto ) );
        }

        return set1;
    }

    protected ActivityEntity activityDtoToActivityEntity(ActivityDto activityDto) {
        if ( activityDto == null ) {
            return null;
        }

        ActivityEntity activityEntity = new ActivityEntity();

        activityEntity.setId( activityDto.getId() );
        activityEntity.setName( activityDto.getName() );
        activityEntity.setNameAr( activityDto.getNameAr() );
        activityEntity.setEnabled( activityDto.isEnabled() );
        activityEntity.setTagNames( activityTagNameDtoSetToTagNameEntitySet( activityDto.getTagNames() ) );
        activityEntity.setCreatedBy( activityDto.getCreatedBy() );
        activityEntity.setCreatedDate( activityDto.getCreatedDate() );

        return activityEntity;
    }

    protected BundleEntity bundleDtoToBundleEntity(BundleDto bundleDto) {
        if ( bundleDto == null ) {
            return null;
        }

        BundleEntity bundleEntity = new BundleEntity();

        bundleEntity.setId( bundleDto.getId() );
        bundleEntity.setName( bundleDto.getName() );
        bundleEntity.setNameAr( bundleDto.getNameAr() );
        bundleEntity.setPeriod( bundleDto.getPeriod() );
        bundleEntity.setPrice( bundleDto.getPrice() );
        bundleEntity.setLimitedPeriod( bundleDto.isLimitedPeriod() );
        bundleEntity.setCurrency( bundleDto.getCurrency() );
        bundleEntity.setLimitedToNumberOfUsers( bundleDto.isLimitedToNumberOfUsers() );
        bundleEntity.setNumberOfUsersLimit( bundleDto.getNumberOfUsersLimit() );
        bundleEntity.setNumberOfUsers( bundleDto.getNumberOfUsers() );
        bundleEntity.setLimitedToNumberOfClients( bundleDto.isLimitedToNumberOfClients() );
        bundleEntity.setNumberOfClientsLimit( bundleDto.getNumberOfClientsLimit() );
        bundleEntity.setNumberOfClients( bundleDto.getNumberOfClients() );
        bundleEntity.setEnabled( bundleDto.isEnabled() );

        return bundleEntity;
    }

    protected SubscriptionEntity subscriptionDtoToSubscriptionEntity(SubscriptionDto subscriptionDto) {
        if ( subscriptionDto == null ) {
            return null;
        }

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        subscriptionEntity.setId( subscriptionDto.getId() );
        subscriptionEntity.setStartDate( subscriptionDto.getStartDate() );
        subscriptionEntity.setEndDate( subscriptionDto.getEndDate() );
        subscriptionEntity.setExpired( subscriptionDto.isExpired() );
        subscriptionEntity.setPaused( subscriptionDto.isPaused() );
        subscriptionEntity.setPausedSince( subscriptionDto.getPausedSince() );
        subscriptionEntity.setBundle( bundleDtoToBundleEntity( subscriptionDto.getBundle() ) );
        subscriptionEntity.setCreatedBy( subscriptionDto.getCreatedBy() );
        subscriptionEntity.setCreatedDate( subscriptionDto.getCreatedDate() );

        return subscriptionEntity;
    }

    protected Set<SubscriptionEntity> subscriptionDtoSetToSubscriptionEntitySet(Set<SubscriptionDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<SubscriptionEntity> set1 = new HashSet<SubscriptionEntity>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( SubscriptionDto subscriptionDto : set ) {
            set1.add( subscriptionDtoToSubscriptionEntity( subscriptionDto ) );
        }

        return set1;
    }

    protected ClientEntity clientDtoToClientEntity(ClientDto clientDto) {
        if ( clientDto == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setId( clientDto.getId() );
        clientEntity.setName( clientDto.getName() );
        clientEntity.setNameAr( clientDto.getNameAr() );
        clientEntity.setContactInfo( clientDto.getContactInfo() );
        clientEntity.setProperties( clientDto.getProperties() );
        clientEntity.setEnabled( clientDto.isEnabled() );
        clientEntity.setActivity( activityDtoToActivityEntity( clientDto.getActivity() ) );
        clientEntity.setSubscriptions( subscriptionDtoSetToSubscriptionEntitySet( clientDto.getSubscriptions() ) );
        clientEntity.setCreatedBy( clientDto.getCreatedBy() );
        clientEntity.setCreatedDate( clientDto.getCreatedDate() );

        return clientEntity;
    }
}
