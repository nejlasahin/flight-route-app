import axios from 'axios';
import {Button, Form, Modal} from 'react-bootstrap';
import {useCallback, useEffect, useReducer, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {MdDelete, MdEdit} from 'react-icons/md';

const initialTransportationState = {
    origin_location_id: '',
    destination_location_id: '',
    transportation_type: 'FLIGHT',
    operating_days: []
};

const transportationReducer = (state, action) => {
    switch (action.type) {
        case 'SET_FIELD':
            return {...state, [action.field]: action.value};
        case 'SET_OPERATING_DAYS':
            return {...state, operating_days: action.value};
        case 'RESET':
            return {...initialTransportationState};
        default:
            return state;
    }
};

function Transportations() {
    const [data, setData] = useState([]);
    const [locations, setLocations] = useState([]);
    const [responseAlert, setResponseAlert] = useState("");
    const [newTransportation, dispatch] = useReducer(transportationReducer, initialTransportationState);
    const [show, setShow] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [currentTransportation, setCurrentTransportation] = useState(null);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [transportationToDelete, setTransportationToDelete] = useState(null);

    const handleClose = () => setShow(false);
    const handleShow = (transportation = null) => {
        setIsEditing(transportation !== null);
        setCurrentTransportation(transportation);
        dispatch({type: 'RESET'});

        if (transportation) {
            Object.keys(transportation).forEach(key => {
                if (key === 'origin_location' || key === 'destination_location') {
                    dispatch({type: 'SET_FIELD', field: `${key}_id`, value: transportation[key].id});
                } else {
                    dispatch({type: 'SET_FIELD', field: key, value: transportation[key]});
                }
            });
        }
        setResponseAlert("");
        setShow(true);
    };

    const fetchLocations = useCallback(() => {
        axios.get(`/api/v1/locations`)
            .then((res) => setLocations(res.data.data))
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while fetching locations.';
                setResponseAlert(errorMessage);
            });
    }, []);

    const fetchTransportations = useCallback(() => {
        axios.get(`/api/v1/transportations`)
            .then((res) => setData(res.data.data))
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while fetching transportations.';
                setResponseAlert(errorMessage);
            });
    }, []);

    useEffect(() => {
        fetchTransportations();
        fetchLocations();
    }, [fetchTransportations, fetchLocations]);

    function confirmDeleteTransportation(item) {
        setTransportationToDelete(item);
        setShowDeleteModal(true);
    }

    function handleDeleteConfirmed() {
        if (!transportationToDelete) return;

        axios.delete(`/api/v1/transportations/${transportationToDelete.id}`)
            .then((res) => {
                setResponseAlert(res.data.message);
                fetchTransportations();
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while deleting the transportation.';
                setResponseAlert(errorMessage);
            })
            .finally(() => {
                setShowDeleteModal(false);
                setTransportationToDelete(null);
            });
    }

    function addTransportation() {
        axios.post(`/api/v1/transportations`, newTransportation, {
            headers: {'Content-Type': 'application/json'}
        })
            .then(() => {
                handleClose();
                fetchTransportations();
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while adding the transportation.';
                setResponseAlert(errorMessage);
            });
    }

    function updateTransportation() {
        if (!currentTransportation) return;

        axios.put(`/api/v1/transportations/${currentTransportation.id}`, newTransportation, {
            headers: {'Content-Type': 'application/json'}
        })
            .then(() => {
                handleClose();
                fetchTransportations();
            })
            .catch((err) => {
                const errorMessage = err.response?.data?.message || 'An error occurred while updating the transportation.';
                setResponseAlert(errorMessage);
            });
    }

    function handleInputChange(e) {
        const {name, value} = e.target;
        dispatch({type: 'SET_FIELD', field: name, value});
    }

    function handleOperatingDaysChange(e) {
        const {value, checked} = e.target;
        let updatedDays = [...newTransportation.operating_days];

        if (checked) {
            updatedDays.push(parseInt(value));
        } else {
            updatedDays = updatedDays.filter(day => day !== parseInt(value));
        }

        dispatch({type: 'SET_OPERATING_DAYS', value: updatedDays});
    }

    return (
        <>
            <div className="container p-5">
                <div className="mb-4">
                    <Button variant="dark" onClick={() => handleShow()}>
                        Add Transportation
                    </Button>
                </div>

                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{isEditing ? 'Edit Transportation' : 'Add Transportation'}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {responseAlert && (
                            <div className="alert alert-warning alert-dismissible fade show" role="alert">
                                {responseAlert}
                                <button type="button" className="btn-close" data-bs-dismiss="alert"
                                        aria-label="Close"></button>
                            </div>
                        )}
                        <form>
                            <div className="mb-3">
                                <select
                                    className="form-control"
                                    name="origin_location_id"
                                    value={newTransportation.origin_location_id}
                                    onChange={handleInputChange}
                                >
                                    <option value="">Select Origin Location</option>
                                    {locations.map(location => (
                                        <option key={location.id} value={location.id}>
                                            {location.location_code} - {location.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="mb-3">
                                <select
                                    className="form-control"
                                    name="destination_location_id"
                                    value={newTransportation.destination_location_id}
                                    onChange={handleInputChange}
                                >
                                    <option value="">Select Destination Location</option>
                                    {locations.map(location => (
                                        <option key={location.id} value={location.id}>
                                            {location.location_code} - {location.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="mb-3">
                                <select
                                    className="form-control"
                                    name="transportation_type"
                                    value={newTransportation.transportation_type}
                                    onChange={handleInputChange}
                                >
                                    <option value="FLIGHT">FLIGHT</option>
                                    <option value="BUS">BUS</option>
                                    <option value="UBER">UBER</option>
                                    <option value="SUBWAY">SUBWAY</option>
                                </select>
                            </div>
                            <div className="mb-3">
                                <label>Operating Days:</label>
                                <div>
                                    {[1, 2, 3, 4, 5, 6, 7].map(day => (
                                        <Form.Check
                                            key={day}
                                            inline
                                            type="checkbox"
                                            label={`Day ${day}`}
                                            value={day}
                                            checked={newTransportation.operating_days.includes(day)}
                                            onChange={handleOperatingDaysChange}
                                        />
                                    ))}
                                </div>
                            </div>
                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={handleClose}>Close</Button>
                        <Button variant="dark" onClick={isEditing ? updateTransportation : addTransportation}>
                            {isEditing ? 'Update' : 'Add'}
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>Confirm Delete</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        Are you sure you want to delete transportation
                        <br/>
                        <strong>ID:</strong> {transportationToDelete?.id} <br/>
                        <strong>Origin
                            Location:</strong> {transportationToDelete?.origin_location?.location_code} - {transportationToDelete?.origin_location?.name}
                        <br/>
                        <strong>Destination
                            Location:</strong> {transportationToDelete?.destination_location?.location_code} - {transportationToDelete?.destination_location?.name}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>Cancel</Button>
                        <Button variant="danger" onClick={handleDeleteConfirmed}>Delete</Button>
                    </Modal.Footer>
                </Modal>

                {data.length > 0 ? (
                    <table className="table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Origin Location</th>
                            <th>Destination Location</th>
                            <th>Type</th>
                            <th>Operating Days</th>
                            <th>Update</th>
                            <th>Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        {data.map((item) => (
                            <tr key={item.id}>
                                <td>{item.id}</td>
                                <td>{item.origin_location.location_code} - {item.origin_location.name}</td>
                                <td>{item.destination_location.location_code} - {item.destination_location.name}</td>
                                <td>{item.transportation_type}</td>
                                <td>{item.operating_days.join(', ')}</td>
                                <td>
                                    <button onClick={() => handleShow(item)} className="btn btn-warning">
                                        <MdEdit/>
                                    </button>
                                </td>
                                <td>
                                    <button onClick={() => confirmDeleteTransportation(item)}
                                            className="btn btn-danger">
                                        <MdDelete/>
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <h5 className="text-center">You don't have any transportation. Create one now.</h5>
                )}
            </div>
        </>
    );
}

export default Transportations;
