import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Button, Card, Col, Collapse, Form, Row} from 'react-bootstrap';
import {
    FaAngleRight,
    FaBus,
    FaCar,
    FaChevronDown,
    FaChevronUp,
    FaMapMarkerAlt,
    FaPlane,
    FaSubway
} from 'react-icons/fa';

function RouteCalculation() {
    const [locations, setLocations] = useState([]);
    const [originLocation, setOriginLocation] = useState('');
    const [destinationLocation, setDestinationLocation] = useState('');
    const [travelDate, setTravelDate] = useState('');
    const [routes, setRoutes] = useState([]);
    const [responseAlert, setResponseAlert] = useState('');
    const [showResults, setShowResults] = useState(false);
    const [openRoute, setOpenRoute] = useState(null);

    useEffect(() => {
        axios.get('/api/v1/locations')
            .then((res) => {
                setLocations(res.data.data);
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while fetching locations.';
                setResponseAlert(errorMessage);
            });
    }, []);

    const fetchRoutes = () => {
        if (!originLocation || !destinationLocation || !travelDate) {
            setResponseAlert('Please provide all inputs.');
            return;
        }

        axios.get('/api/v1/routes', {
            params: {
                origin_location_id: originLocation,
                destination_location_id: destinationLocation,
                travel_date: travelDate
            }
        })
            .then(res => {
                setRoutes(res.data.data);
                setShowResults(true);
                setResponseAlert('');
            })
            .catch(err => {
                const errorMessage = err.response?.data?.message || 'An error occurred while fetching routes.';
                setResponseAlert(errorMessage);
            });
    };

    const getTransportationIcon = (type) => {
        switch (type.toLowerCase()) {
            case 'bus':
                return <FaBus/>;
            case 'uber':
                return <FaCar/>;
            case 'subway':
                return <FaSubway/>;
            case 'flight':
                return <FaPlane/>;
            default:
                return <FaPlane/>;
        }
    };

    return (
        <div className="container p-5">
            {responseAlert && (
                <div className="alert alert-warning alert-dismissible fade show" role="alert">
                    {responseAlert}
                    <button type="button" className="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            )}

            <Form>
                <Row className="mb-3">
                    <Col md={6}>
                        <Form.Group>
                            <Form.Label>Origin Location</Form.Label>
                            <Form.Control
                                as="select"
                                value={originLocation}
                                onChange={(e) => setOriginLocation(e.target.value)}
                            >
                                <option value="">Select Origin Location</option>
                                {locations.map((location) => (
                                    <option key={location.id} value={location.id}>
                                        {location.location_code} - {location.name}
                                    </option>
                                ))}
                            </Form.Control>
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group>
                            <Form.Label>Destination Location</Form.Label>
                            <Form.Control
                                as="select"
                                value={destinationLocation}
                                onChange={(e) => setDestinationLocation(e.target.value)}
                            >
                                <option value="">Select Destination Location</option>
                                {locations.map((location) => (
                                    <option key={location.id} value={location.id}>
                                        {location.location_code} - {location.name}
                                    </option>
                                ))}
                            </Form.Control>
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    <Form.Label>Travel Date</Form.Label>
                    <Form.Control
                        type="date"
                        value={travelDate}
                        onChange={(e) => setTravelDate(e.target.value)}
                    />
                </Form.Group>

                <Button variant="dark" onClick={fetchRoutes}>Get Routes</Button>
            </Form>

            {showResults && (
                <div className="mt-4">
                    {routes.length > 0 ? (
                        <div>
                            {routes.map((route, index) => (
                                <Card className="mb-3" key={index} style={{cursor: 'pointer'}}>
                                    <Card.Body>
                                        <div className="d-flex justify-content-between align-items-center mb-2">
                                            <div>
                                                <strong>Route {index + 1}</strong>
                                                <div className="text-muted">
                                                    {route.transportations.map((transportation, transportIndex) => (
                                                        <span key={transportIndex}>
                                                            {transportation.origin_location.location_code}
                                                            {transportIndex < route.transportations.length - 1 ?
                                                                <FaAngleRight/> : ''}
                                                        </span>
                                                    ))}
                                                    <FaAngleRight/>
                                                    {route.transportations[route.transportations.length - 1].destination_location.location_code}
                                                </div>
                                            </div>
                                            <Button
                                                variant="link"
                                                onClick={() => setOpenRoute(openRoute === index ? null : index)}
                                                style={{
                                                    fontSize: '1rem',
                                                    color: '#dc3545',
                                                    padding: 0,
                                                    textDecoration: 'none'
                                                }}
                                            >
                                                {openRoute === index ? (
                                                    <>
                                                        <FaChevronUp/> Hide Details
                                                    </>
                                                ) : (
                                                    <>
                                                        <FaChevronDown/> Show Details
                                                    </>
                                                )}
                                            </Button>
                                        </div>
                                        <Collapse in={openRoute === index}>
                                            <div>
                                                <ul className="list-unstyled">
                                                    {route.transportations.map((transportation, transportIndex) => (
                                                        <li key={transportIndex} className="d-flex align-items-center">
                                                            <div className="me-3">
                                                                {getTransportationIcon(transportation.transportation_type)}
                                                            </div>
                                                            <div>
                                                                <strong>{transportation.transportation_type}</strong> &nbsp; - &nbsp;
                                                                {transportation.origin_location.location_code} ({transportation.origin_location.name}) <FaMapMarkerAlt/> {transportation.destination_location.location_code} ({transportation.destination_location.name})
                                                            </div>
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        </Collapse>
                                    </Card.Body>
                                </Card>
                            ))}
                        </div>
                    ) : (
                        <p>No routes available for the selected criteria.</p>
                    )}
                </div>
            )}
        </div>
    );
}

export default RouteCalculation;
